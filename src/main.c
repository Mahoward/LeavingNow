#include "menu_window.h"

#define KEY_DATA 5

/* Alert window appears when message is recieved*/
static Window *i_message_window;
static TextLayer *i_text_layer;

static void i_message_window_load(Window *window){
  Layer *window_layer = window_get_root_layer(window);
  GRect window_bounds = layer_get_bounds(window_layer);

  // Create output TextLayer
  i_text_layer = text_layer_create(GRect(5, 0, window_bounds.size.w - 5, window_bounds.size.h));
  text_layer_set_font(i_text_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24));
  text_layer_set_text(i_text_layer, "Waiting...");
  text_layer_set_overflow_mode(i_text_layer, GTextOverflowModeWordWrap);
  layer_add_child(window_layer, text_layer_get_layer(i_text_layer));
}

static void i_message_window_unload(){
  // Destroy output TextLayer
  text_layer_destroy(i_text_layer);
}


static void inbox_received_callback(DictionaryIterator *iterator, void *context) {
  // Get the first pair
  Tuple *t = dict_read_first(iterator);

  // Process all pairs present
  while (t != NULL) {
    // Long lived buffer
    static char s_buffer[64];

    // Process this pair's key
    switch (t->key) {
      case KEY_DATA:
        window_stack_push(i_message_window, true);
        // Copy value and display
        snprintf(s_buffer, sizeof(s_buffer), "Received '%s'", t->value->cstring);
        text_layer_set_text(i_text_layer, s_buffer);
        
        DictionaryIterator *iter;
        Tuplet test_tuple =  TupletInteger(KEY_DATA, 55);
        app_message_outbox_begin(&iter);
        dict_write_tuplet(iter, &test_tuple);
        dict_write_end(iter);
        app_message_outbox_send();
        
        break;
    }

    // Get next pair, if any
    t = dict_read_next(iterator);
  }
}

static void inbox_dropped_callback(AppMessageResult reason, void *context) {
  APP_LOG(APP_LOG_LEVEL_ERROR, "Message dropped!");
}

static void outbox_failed_callback(DictionaryIterator *iterator, AppMessageResult reason, void *context) {
  APP_LOG(APP_LOG_LEVEL_ERROR, "Outbox send failed!");
}

static void outbox_sent_callback(DictionaryIterator *iterator, void *context) {
  APP_LOG(APP_LOG_LEVEL_INFO, "Outbox send success!");
}
  
static void init() {
  
  // Register callbacks
  app_message_register_inbox_received(inbox_received_callback);
  app_message_register_inbox_dropped(inbox_dropped_callback);
  app_message_register_outbox_failed(outbox_failed_callback);
  app_message_register_outbox_sent(outbox_sent_callback);

  // Open AppMessage
  app_message_open(app_message_inbox_size_maximum(), app_message_outbox_size_maximum());

  
  main_window = window_create();

  window_set_window_handlers(main_window, (WindowHandlers) {
    .load = menu_window_load,
    .unload = menu_window_unload,
  });
  
  i_message_window = window_create();
        
  window_set_window_handlers(i_message_window, (WindowHandlers) {
    .load = i_message_window_load,
    .unload = i_message_window_unload,
  });

  window_stack_push(main_window, true);

}

static void deinit(){
  window_destroy(main_window);
}

int main(void){
  init();
  app_event_loop();
  deinit();
}