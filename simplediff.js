var diff, htmlDiff, stringDiff;

diff = function(before, after) {
  var i, j, k, lastRow, ohash, subLength, subStartAfter, subStartBefore, thisRow, val, _i, _len, _len2, _len3, _ref, _ref2;
  ohash = {};
  for (i = 0, _len = before.length; i < _len; i++) {
    val = before[i];
    if (!(val in ohash)) ohash[val] = [];
    ohash[val].push(i);
  }
  lastRow = (function() {
    var _ref, _results;
    _results = [];
    for (i = 0, _ref = before.length; 0 <= _ref ? i < _ref : i > _ref; 0 <= _ref ? i++ : i--) {
      _results.push(0);
    }
    return _results;
  })();
  subStartBefore = subStartAfter = subLength = 0;
  for (j = 0, _len2 = after.length; j < _len2; j++) {
    val = after[j];
    thisRow = (function() {
      var _ref, _results;
      _results = [];
      for (i = 0, _ref = before.length; 0 <= _ref ? i < _ref : i > _ref; 0 <= _ref ? i++ : i--) {
        _results.push(0);
      }
      return _results;
    })();
    _ref2 = (_ref = ohash[val]) != null ? _ref : [];
    for (_i = 0, _len3 = _ref2.length; _i < _len3; _i++) {
      k = _ref2[_i];
      thisRow[k] = (k && lastRow[k - 1] ? 1 : 0) + 1;
      if (thisRow[k] > subLength) {
        subLength = thisRow[k];
        subStartBefore = k - subLength + 1;
        subStartAfter = j - subLength + 1;
      }
    }
    lastRow = thisRow;
  }
  if (subLength === 0) {
    return [].concat((before.length ? [['-', before]] : []), (after.length ? [['+', after]] : []));
  } else {
    return [].concat(diff(before.slice(0, subStartBefore), after.slice(0, subStartAfter)), [['=', after.slice(subStartAfter, (subStartAfter + subLength))]], diff(before.slice(subStartBefore + subLength), after.slice(subStartAfter + subLength)));
  }
};

stringDiff = function(before, after) {
  return diff(before.split(/[ ]+/), after.split(/[ ]+/));
};

htmlDiff = function(before, after) {
  var a, b, con;
  con = {
    '=': (function(x) {
      return x;
    }),
    '+': (function(x) {
      return '<ins>' + x + '</ins>';
    }),
    '-': (function(x) {
      return '<del>' + x + '</del>';
    })
  };
  return ((function() {
    var _i, _len, _ref, _ref2, _results;
    _ref = stringDiff(before, after);
    _results = [];
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      _ref2 = _ref[_i], a = _ref2[0], b = _ref2[1];
      _results.push(con[a](b.join(' ')));
    }
    return _results;
  })()).join(' ');
};
