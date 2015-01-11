#include "favorites.h"

#define NUM_F_SECTIONS 1
#define NUM_F_ITEMS 3

static SimpleMenuItem fav_menu[NUM_F_ITEMS];
static SimpleMenuLayer *fav_menu_layer;
static Window *fav_window;
static SimpleMenuSection fav_menu_sections[NUM_F_SECTIONS];
static GBitmap *fav_icon_image;

static void generic_callback(int index, void *ctx) {
  fav_menu[index].subtitle = "Selected";
  layer_mark_dirty(simple_menu_layer_get_layer(fav_menu_layer));
}

void fav_window_load(Window *window){
  int curr_item = 0;

  fav_menu[curr_item++] = (SimpleMenuItem){
    .title = "F_Ryan",
    .callback = generic_callback,
  };

  fav_menu[curr_item++] = (SimpleMenuItem){
    .title = "F_Fedor",
    .callback = generic_callback,
  };

  fav_menu[curr_item++] = (SimpleMenuItem){
    .title = "F_Mike",
    .callback = generic_callback,
    .icon = fav_icon_image,
  };

  fav_menu_sections[0] = (SimpleMenuSection){
    .num_items = NUM_F_ITEMS,
    .items = fav_menu,
  };
  
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_frame(window_layer);

  fav_menu_layer = simple_menu_layer_create(bounds, window, fav_menu_sections, NUM_F_SECTIONS, NULL);

  layer_add_child(window_layer, simple_menu_layer_get_layer(fav_menu_layer));
}


void fav_window_unload(Window *window) {
  simple_menu_layer_destroy(fav_menu_layer);

  gbitmap_destroy(fav_icon_image);
}

void fav_list_callback(int index, void *ctx) {
  fav_window = window_create();

  window_set_window_handlers(fav_window, (WindowHandlers) {
    .load = fav_window_load,
    .unload = fav_window_unload,
  });

  window_stack_push(fav_window, true);

}
