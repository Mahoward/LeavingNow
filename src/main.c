#include "pebble.h"

#define NUM_MM_SECTIONS 1
#define NUM_ITEMS 3

static Window *window;

static SimpleMenuLayer *simple_menu_layer;

static SimpleMenuSection menu_sections[NUM_MM_SECTIONS];

static SimpleMenuItem main_menu[NUM_ITEMS];

static GBitmap *menu_icon_image;

static void menu_select_callback(int index, void *ctx) {
  main_menu[index].subtitle = "You've hit select here!";
  layer_mark_dirty(simple_menu_layer_get_layer(simple_menu_layer));
}

static void window_load(Window *window) {
  int curr_item = 0;

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Recent",
    .callback = menu_select_callback,
  };

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Favorites",
    .subtitle = "Here's a subtitle",
    .callback = menu_select_callback,
  };

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Contacts",
    .subtitle = "This has an icon",
    .callback = menu_select_callback,
    .icon = menu_icon_image,
  };

  menu_sections[0] = (SimpleMenuSection){
    .num_items = NUM_ITEMS,
    .items = main_menu,
  };
  
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_frame(window_layer);

  simple_menu_layer = simple_menu_layer_create(bounds, window, menu_sections, NUM_MM_SECTIONS, NULL);

  layer_add_child(window_layer, simple_menu_layer_get_layer(simple_menu_layer));
}

void window_unload(Window *window) {
  simple_menu_layer_destroy(simple_menu_layer);

  gbitmap_destroy(menu_icon_image);
}

int main(void) {
  window = window_create();

  window_set_window_handlers(window, (WindowHandlers) {
    .load = window_load,
    .unload = window_unload,
  });

  window_stack_push(window, true /* Animated */);

  app_event_loop();

  window_destroy(window);
}