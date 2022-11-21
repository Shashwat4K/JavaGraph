import os
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import pandas as pd
import numpy as np
import geopandas as gpd
from shapely.geometry import Point, LineString
from rich import print
from tqdm import tqdm 

def load_states_data(states_file: str):
    """Load the US states `geopandas` shape file data"""
    if not os.path.exists(states_file):
        raise Exception(f"File {states_file} does not exist. Please enter correct path.")
    if not states_file.endswith(".shp"):
        # Only accept .shp files
        raise Exception("Only '.shp' files are accepted")
    states = gpd.read_file(states_file)
    return states

def load_cities_data(cities_file: str):
    """Load the data of US cities from a CSV file"""
    if not os.path.exists(cities_file):
        raise Exception(f"File {cities_file} does not exist. Please enter correct path.")
    if not cities_file.endswith(".csv"):
        # Only open .csv files
        raise Exception("Only '.csv' files are accepted")
    cities = pd.read_csv(cities_file)
    cities = cities[['city', 'city_ascii', 'state_id', 'state_name', 'population', 'lat', 'lng', 'id', 'value']]
    return cities

def plot_data(states, cities, plot_lines, ax, **kwargs):
    """Plot the data"""
    city_coords = get_coords(cities)
    xs = city_coords['X']
    ys = city_coords['Y']
    # figsize = (8, 6) if not kwargs.get("figsize") else kwargs.get("figsize")
    states_kw = kwargs.get('states_kw', {})
    scatter_kw = kwargs.get('scatter_kw', {})
    plot_kw = kwargs.get('plot_kw', {})
    # Plot the states
    states.apply(lambda x: ax.annotate(text=x.STUSPS, xy=x.geometry.centroid.coords[0], ha='center', fontsize=5.5),axis=1)
    states.boundary.plot(
        ax=ax, 
        linewidth=states_kw.get('linewidth', 0.4),
        color=states_kw.get("color", "Black")
    )
    states.plot(ax=ax, cmap=states_kw.get("cmap", "Pastel2"))
    # Plot the points
    ax.scatter(xs, ys, 
        marker=scatter_kw.get('marker', 'x'), 
        s=scatter_kw.get('s', 4), 
        color=scatter_kw.get('color', 'blue'), 
        zorder=scatter_kw.get('zorder', 2)
    )
    # Plot the lines
    for line in plot_lines:
        x, y = line.xy
        ax.plot(x, y, 
            color=plot_kw.get('color', '#6699cc'), 
            alpha=plot_kw.get('alpha', 0.7), 
            linewidth=plot_kw.get('linewidth', 3), 
            solid_capstyle=plot_kw.get('solid_capstyle', 'round'), 
            zorder=plot_kw.get('zorder', 1)
        )
    

def get_coords(cities):
    coords = dict()
    coords['X'] = []
    coords['Y'] = []
    for val in range(len(cities)):
        lat, lng = get_lat_lng(val, cities)
        coords['X'].append(lng)
        coords['Y'].append(lat)
    return coords

def get_vertex_value(city_info, df):
    city, state = city_info.split(',')
    value = int(df.loc[(df['state_id'] == state) & (df['city'] == city)]['value'])
    return value

def get_lat_lng(val, cities):
    lat, lng = cities.loc[cities['value'] == val][['lat', 'lng']].values[0]
    return lat, lng

def get_plot_lines_from_text_file(txt_file_path: str, cities):
    """Get the graph edges"""
    edges_v = []
    with open(txt_file_path, "r") as f:
        lines = f.readlines()
        for line in tqdm(lines, desc="Reading edges..."):
            src, dest = map(int, line.split())
            edges_v.append((src, dest))
    plot_lines = []
    for edge in tqdm(edges_v, desc="Getting plot lines..."):
        src, dest = edge
        src_lat, src_lng = cities.loc[cities['value'] == src][['lat', 'lng']].values[0]
        dest_lat, dest_lng = cities.loc[cities['value'] == dest][['lat', 'lng']].values[0]
        plot_line = LineString([(src_lng, src_lat), (dest_lng, dest_lat)])
        plot_lines.append(plot_line)
    return plot_lines
        

def live_plot_animate(i, filepath, filenames, states, cities, plot_lines, ax, kwargs):
    info_dict = dict()
    with open(os.path.join(filepath, filenames[i]),'r') as f:
        file_lines = f.readlines()
        for line in file_lines:
            tokens = line.split()
            info_dict[int(tokens[0])] = dict()
            info_dict[int(tokens[0])]['visited'] = True if tokens[1] == '1' else False
            info_dict[int(tokens[0])]['isAP'] = True if tokens[2] == '1' else False
    states_kw = kwargs.get('states_kw', {})
    scatter_kw = kwargs.get('scatter_kw', {})
    plotline_kw = kwargs.get('plotline_kw', {})
    ax.clear()
    states.apply(lambda x: ax.annotate(text=x.STUSPS, xy=x.geometry.centroid.coords[0], 
        ha='center', fontsize=5.5, color='White'), #states_kw.get('ha', 'center'), fontsize=states_kw.get('fontsize', 5.5), 
        axis=1)
    states.boundary.plot(ax=ax, linewidth=states_kw.get('boundary_linewidth', 0.4), color=states_kw.get('boundary_color', 'Black'))
    states.plot(ax=ax, cmap=states_kw.get('cmap', 'Pastel1'))
    city_coords = get_coords(cities)
    xs = city_coords['X']
    ys = city_coords['Y']
    ax.scatter(xs, ys, 
        marker=scatter_kw.get('normal_marker', 'x'), 
        s=scatter_kw.get('normal_s', 4), 
        color=scatter_kw.get('normal_color', 'black'), 
        zorder=2
    )
    visited_xs = []
    visited_ys = []
    isAP_xs = []
    isAP_ys = []
    for k, v in info_dict.items():
        curr_y, curr_x = get_lat_lng(k, cities)
        if v['visited']:
            visited_xs.append(curr_x)
            visited_ys.append(curr_y)
        if v['isAP']:
            isAP_xs.append(curr_x)
            isAP_ys.append(curr_y)
    ax.scatter(visited_xs, visited_ys, 
        marker=scatter_kw.get('visited_marker', 'o'), 
        s=scatter_kw.get('visited_s', 10), 
        color=scatter_kw.get('visited_color', 'yellow'), 
        linewidth=scatter_kw.get('linewidth', 4),
        zorder=3
    )
    ax.scatter(isAP_xs, isAP_ys, 
        marker=scatter_kw.get('ap_marker', 'o'), 
        s=scatter_kw.get('ap_s', 10), 
        color=scatter_kw.get('ap_color', 'red'), 
        linewidth=scatter_kw.get('linewidth', 4),
        zorder=4
    )
    for line in plot_lines:
        x, y = line.xy
        ax.plot(x, y,
            zorder=1,
            **plotline_kw
        )
    gca = plt.gca()
    gca.get_xaxis().set_visible(False)
    gca.get_yaxis().set_visible(False)
    gca.get_xaxis().set_ticks([])
    gca.get_yaxis().set_ticks([])
    ax.margins(x=0, y=0)
    ax.set_title(f"Frame {i} File: {filenames[i]}")
    

def main(**kwargs):
    """Main Function"""
    DATA_ROOT = kwargs.get('data_root', "../GeoData")
    PLOT_DATA_ROOT = kwargs.get('plot_data_root', "../data/demo_data")
    save_loc = kwargs.get('save_loc')
    states_file = os.path.join(DATA_ROOT, "usa-states-census-2014.shp")
    cities_file = os.path.join(DATA_ROOT, "filtered_uscities_data.csv")
    graph_edges_file_path = os.path.join(DATA_ROOT, "usdemo_graph.txt")
    print(f"Loading states data from [green]{states_file}[/]")
    states = load_states_data(states_file)
    print(f"Loading cities data from [green]{cities_file}[/]")
    cities = load_cities_data(cities_file)

    print(f"Loading plot lines from [green]{graph_edges_file_path}[/]")
    plot_lines = get_plot_lines_from_text_file(graph_edges_file_path, cities)
    fig, ax = plt.subplots(figsize=kwargs.get('figsize', (12, 5)))
    
    file_names = [] 
    for fname in os.listdir(PLOT_DATA_ROOT):
        if os.path.isfile(os.path.join(PLOT_DATA_ROOT, fname)) and fname.endswith(".txt"):
            file_names.append(fname)
    file_names = sorted(file_names)
    ani = animation.FuncAnimation(
        fig, live_plot_animate, 
        fargs=(PLOT_DATA_ROOT, file_names, states, cities, plot_lines, ax, kwargs), 
        frames=len(file_names), 
        interval=kwargs.get('interval', 1000)
    )
    plt.axis("off")
    if save_loc:
        print(f"Saving at {save_loc}")
        ani.save(save_loc, fps=2)
    if kwargs.get('show_fullscreen'):
        fig_manager = plt.get_current_fig_manager()
        fig_manager.full_screen_toggle()
    plt.subplots_adjust(left=0.01, bottom=0.01, right=1.0, top=1.0)
    plt.show()

if __name__ == "__main__":
    states_kw = {'cmap': 'viridis', 'boundary_color':'white'}
    scatter_kw = {'normal_marker':'o', 'visited_s': 150, 'visited_color': 'cyan', 'visited_marker':'*', 'ap_s': 90, 'ap_marker': 'x'}
    plotline_kw = {'color': 'yellow', 'alpha': 1.0}
    main(
        figsize=(20, 8),
        states_kw=states_kw, scatter_kw=scatter_kw, plotline_kw=plotline_kw, show_fullscreen=True
    )