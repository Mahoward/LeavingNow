#include "recent.h"

#define NUM_R_SECTIONS 1
#define NUM_R_ITEMS 4
#define R_KEY 2

static SimpleMenuItem recent_menu[NUM_R_ITEMS];
static SimpleMenuLayer *recent_menu_layer;
static Window *recent_window;
static SimpleMenuSection recent_menu_sections[NUM_R_SECTIONS];
static GBitmap *recent_icon_image;

static void send_recent_callback(int index, void *ctx) {
  DictionaryIterator *iter;
  Tuplet test_tuple =  TupletCString(R_KEY, recent_menu[index].title);
  app_message_outbox_begin(&iter);
  dict_write_tuplet(iter, &test_tuple);
  dict_write_end(iter);
  app_message_outbox_send();
}

void populate_recents(){
  recent_menu[0] = (SimpleMenuItem){
    .title = recent_name[0],
    .callback = send_recent_callback,
  };

  recent_menu[1] = (SimpleMenuItem){
    .title = recent_name[1],
    .callback = send_recent_callback,
  };

  recent_menu[2] = (SimpleMenuItem){
    .title = recent_name[2],
    .callback = send_recent_callback,
    .icon = recent_icon_image,
  };
    
  recent_menu[3] = (SimpleMenuItem){
    .title = recent_name[3],
    .callback = send_recent_callback,
    .icon = recent_icon_image,
  };

  recent_menu_sections[0] = (SimpleMenuSection){
    .num_items = NUM_R_ITEMS,
    .items = recent_menu,
  };
}

void recent_window_load(Window *window){
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_frame(window_layer);

  recent_menu_layer = simple_menu_layer_create(bounds, window, recent_menu_sections, NUM_R_SECTIONS, NULL);

  layer_add_child(window_layer, simple_menu_layer_get_layer(recent_menu_layer));
}


void recent_window_unload(Window *window) {
  simple_menu_layer_destroy(recent_menu_layer);

  gbitmap_destroy(recent_icon_image);
}

void recent_list_callback(int index, void *ctx) {
  recent_window = window_create();

  window_set_window_handlers(recent_window, (WindowHandlers) {
    .load = recent_window_load,
    .unload = recent_window_unload,
  });

  window_stack_push(recent_window, true);

}
