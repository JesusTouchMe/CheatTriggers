// stolen from chattriggers

let global = this;

global.Java = {
    type: clazz => Packages[clazz]
};

global.Logger = Java.type("cum.jesus.cheattriggers.utils.Logger");
global.Thread = Java.type("java.lang.Thread");

global.print = function (toPrint) {
    if (toPrint === null) {
        toPrint = "null";
    } else if (toPrint === undefined) {
        toPrint = "undefined";
    }

    Logger.info(toPrint);
}

global.setTimeout = function (func, delay) {
    new Thread(function () {
        Thread.sleep(delay);
        func();
    }).start();
};

// console
(function (globalObj, factory) {
    Object.defineProperty(globalObj, "console", {
        "writable": true,
        "configurable": true,
        "enumerable": false,
        "value": factory()
    });
}(global, function factory() {
    const console = {};

    console.config = {
        "indent": " | ",
        "showMilliseconds": false,
        "showMessageType": false,
        "showTimeStamp": false,
        "useColors": false,
        "colorsByLogLevel": {
            "error": "",
            "log": "",
            "info": "",
            "warn": ""
        }
    };

    const _ = {};

    _.counters = {};

    _.defaultStringifier = function (anyValue) {
        switch (typeof anyValue) {
            case "function":
                return "[object Function(" + anyValue.length + ")]";
            case "object": {
                if (Array.isArray(anyValue)) return "[object Array(" + anyValue.length + ")]";
            }
            default:
                return String(anyValue);
        }
    };

    _.indentLevel = 0;
    _.lineSep = java.lang.System.getProperty("line.separator");
    _.timers = {};

    _.formats = {
        "%d": (x) => typeof x === "symbol" ? NaN : parseInt(x),
        "%f": (x) => typeof x === "symbol" ? NaN : parseFloat(x),
        "%i": (x) => typeof x === "symbol" ? NaN : parseInt(x),
        "%s": (x) => String(x),
        "%%": (x) => "%",
        "%c": (x) => String(x),
        "%o": (x) => String(x),
        "%O": (x) => String(x)
    };

    _.formatArguments = function (args) {
        const rv = [];
        for (let i = 0; i < args.length; i++) {
            const arg = args[i];
            if (typeof arg === "string") {
                rv.push(arg.replace(/%[cdfiosO%]/g, (match) => {
                    if (match === "%%") return "%";
                    return ++i in args ? _.formats[match](args[i]) : match;
                }));
            } else {
                rv.push(String(arg));
            }
        }
        return rv.join(" ");
    };

    _.formatObject = function (object) {
        const sep = _.lineSep;
        return _.defaultStringifier(object) + (" {" + sep + " ") + Object.keys(object).map(key =>
            _.defaultStringifier(key) + ": " + _.defaultStringifier(object[key])).join("," + sep + " ") + (sep + "}");
    };

    _.makeTimeStamp = function () {
        if (console.config.showMilliseconds) {
            const date = new Date();
            return date.toLocaleTimeString() + "," + date.getMilliseconds();
        }
        return new Date().toLocaleTimeString();
    };

    _.repeat = (s, n) => new Array(n + 1).join(s);

    _.writeln = function (msgType, msg) {
        if (arguments.length < 2) return;
        const showMessageType = console.config.showMessageType;
        const showTimeStamp = console.config.showTimeStamp;
        const msgTypePart = showMessageType ? msgType : "";
        const timeStampPart = showTimeStamp ? _.makeTimeStamp() : "";
        const prefix = (showMessageType || showTimeStamp
            ? "[" + msgTypePart + (msgTypePart && timeStampPart ? " - " : "") + timeStampPart + "] "
            : "");
        const indent = (_.indentLevel > 0
            ? _.repeat(console.config.indent, _.indentLevel)
            : "");
        switch (msgType) {
            case "assert":
            case "error": {
                java.lang.System.err.println(prefix + indent + msg);
                break;
            }
            case "warn": {
                java.lang.System.out.println(prefix + indent + msg);
                break;
            }
            default: {
                java.lang.System.out.println(prefix + indent + msg);
            }
        }
    };

    console.assert = function assert(booleanValue, arg) {
        if (!!booleanValue) return;
        const defaultMessage = "assertion failed";
        if (arguments.length < 2) return _.writeln("assert", defaultMessage);
        if (typeof arg !== "string") return _.writeln("assert", _.formatArguments([defaultMessage, arg]));
        _.writeln("assert", (defaultMessage + ": " + arg));
    };

    console.count = function count(label) {
        label = label || "default";

        label = String(label);
        if (!(label in _.counters)) _.counters[label] = 0;
        _.counters[label]++;
        _.writeln("count", label + ": " + _.counters[label]);
    };

    console.dir = function dir(arg, options) {
        if (Object(arg) === arg) {
            _.writeln("dir", _.formatObject(arg));
            return;
        }
        _.writeln("dir", _.defaultStringifier(arg));
    };

    console.groupEnd = function groupEnd() {
        if (_.indentLevel < 1) return;
        _.indentLevel--;
    };

    console.time = function time(label) {
        label = label || "default";

        label = String(label);
        if (label in _.timers) return;
        _.timers[label] = Date.now();
    };

    console.timeEnd = function timeEnd(label) {
        label = label || "default";

        label = String(label);
        const milliseconds = Date.now() - _.timers[label];
        delete _.timers[label];

        _.writeln("timeEnd", label + ": " + milliseconds + " ms");
    };

    console.table = function table(tabularData, properties) {
        console.log(tabularData);
    };

    return console;
}))