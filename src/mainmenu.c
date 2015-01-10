#include <pebble.h>
#inclue "mainmenu.h"

static void main_window_load(Window *window) {
  int curr_item = 0;

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Recent",
    .callback = recent_list_callback,
  };

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Favorites",
    .callback = fav_list_callback,
  };

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Contacts",
    .callback = contact_list_callback,
    .icon = menu_icon_image,
  };

  main_menu_sections[0] = (SimpleMenuSection){
    .num_items = NUM_ITEMS,
    .items = main_menu,
  };
  
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_frame(window_layer);

  main_menu_layer = simple_menu_layer_create(bounds, window, main_menu_sections, NUM_MM_SECTIONS, NULL);

  layer_add_child(window_layer, simple_menu_layer_get_layer(main_menu_layer));
}


void main_window_unload(Window *window) {
  simple_menu_layer_destroy(main_menu_layer);

  gbitmap_destroy(menu_icon_image);
}