WebViewJsBridge
===============

An Android bridge between Java and JavaScript.

## Usage

```javascript

var $ = function(selector) {
  return document.querySelector(selector);
};
var callback = function() {
  $('#pushView').addEventListener('click', function() {
    JSBridge.invoke('pushView', {
      url: 'file:///android_asset/index.html'
    });
  }, false);
  $('#popView').addEventListener('click', function() {
    JSBridge.invoke('popView');
  }, false);
  $('#setTitle').addEventListener('click', function() {
    JSBridge.invoke('setTitle', 'newTitle');
  }, false);
};

if (typeof JSBridge !== 'undefined') {
  callback();
} else {
  document.addEventListener('JSBridgeReady', function() {
    callback();
  }, false);
}

```

```java

setDistUrl("file:///android_asset/index.html");

hideLeftButton();

setLeftText("back");

```

## License

The MIT License (MIT)

Copyright (c) 2015 xdf
