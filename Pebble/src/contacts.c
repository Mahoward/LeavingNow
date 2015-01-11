#include <pebble.h>
#include "contacts.h"

#define NUM_C_SECTIONS 1
#define NUM_C_ITEMS 3

static SimpleMenuItem contact_menu[NUM_C_ITEMS];
static SimpleMenuLayer *contact_menu_layer;
static Window *contact_window;
static SimpleMenuSection contact_menu_sections[NUM_C_SECTIONS];
static GBitmap *contact_icon_image;

static void generic_callback(int index, void *ctx) {
  contact_menu[index].subtitle = "Selected";
  layer_mark_dirty(simple_menu_layer_get_layer(contact_menu_layer));
}

void contact_window_load(Window *window){
  int curr_item = 0;

  contact_menu[curr_item++] = (SimpleMenuItem){
    .title = "C_Ryan",
    .callback = generic_callback,
  };

  contact_menu[curr_item++] = (SimpleMenuItem){
    .title = "C_Fedor",
    .callback = generic_callback,
  };

  contact_menu[curr_item++] = (SimpleMenuItem){
    .title = "C_Mike",
    .callback = generic_callback,
    .icon = contact_icon_image,
  };

  contact_menu_sections[0] = (SimpleMenuSection){
    .num_items = NUM_C_ITEMS,
    .items = contact_menu,
  };
  
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_frame(window_layer);

  contact_menu_layer = simple_menu_layer_create(bounds, window, contact_menu_sections, NUM_C_SECTIONS, NULL);

  layer_add_child(window_layer, simple_menu_layer_get_layer(contact_menu_layer));
}


void contact_window_unload(Window *window) {
  simple_menu_layer_destroy(contact_menu_layer);

  gbitmap_destroy(contact_icon_image);
}

void contact_list_callback(int index, void *ctx) {
  contact_window = window_create();

  window_set_window_handlers(contact_window, (WindowHandlers) {
    .load = contact_window_load,
    .unload = contact_window_unload,
  });

  window_stack_push(contact_window, true);

}

