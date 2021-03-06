package org.projectodd.nodej.bindings;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

public class QueryString extends DynObject {
    
    public QueryString(GlobalObject globalObject) {
        super(globalObject);
        Binding.setProperty(this, "escape", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return QueryString.escape(Types.toString(context, args[0]));
            }
        });
        Binding.setProperty(this, "unescape", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return QueryString.unescape(Types.toString(context, args[0]));
            }
        });
        Binding.setProperty(this, "stringify", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return QueryString.stringify(context, args);
            }
        });
        Binding.setProperty(this, "parse", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return QueryString.parse(context, args);
            }
        });
    }

    private static String stringify(ExecutionContext context, Object[] args) {
        if (args[0] instanceof JSObject) {
            JSObject obj = (JSObject) args[0];
            String sep = "&";
            String eq  = "=";
            if (args[1] != Types.UNDEFINED) {
                sep = Types.toString(context, args[1]);
            }
            if (args[2] != Types.UNDEFINED) {
                eq = Types.toString(context, args[2]);
            }
            StringBuffer string = new StringBuffer();
            for(String name : obj.getAllEnumerablePropertyNames().toList()) {
                final String escapedName = QueryString.escape(name);
                final Object value = obj.get(context, name);
                if (value instanceof DynArray) {
                    DynArray array = (DynArray) value;
                    Long length = Types.toInt32(context, array.get(context, "length"));
                    for (int i=0; i < length; ++i) {
                        string.append(escapedName);
                        string.append(eq);
                        string.append(QueryString.escape(Types.toString(context, array.get(context, ""+i))));
                        if (i < length-1) string.append(sep);
                    }
                    string.append(sep);
                } else {
                    string.append(escapedName);
                    string.append(eq);
                    string.append(QueryString.escape(Types.toString(context, value)));
                    string.append(sep);
                }
            }
            // inefficient way to handle the trailing sep
            return string.toString().substring(0, string.length()-1);
        }
        return "";
    }

    private static JSObject parse(ExecutionContext context, Object[] args) {
        DynObject obj = new DynObject(context.getGlobalObject());
        String string = Types.toString(context, args[0]);
        if (args[1] != Types.UNDEFINED) {
            string = string.replace(Types.toString(context, args[1]), "&");
        }
        if (args[2] != Types.UNDEFINED) {
            string = string.replace(Types.toString(context, args[2]), "=");
        }
        QueryStringDecoder decoder = new QueryStringDecoder("?" + string);
        Map<String, List<String>> parameters = decoder.getParameters();
        for(String key : parameters.keySet()) {
            List<String> values = parameters.get(key);
            if (values.size() == 1) {
                obj.put(context, key, values.get(0), false);
            } else {
                DynArray arr = new DynArray(context.getGlobalObject());
                int i = 0;
                for(String v : values) {
                    arr.put(context, ""+i, Types.toString(context, v), false);
                    i++;
                }
                obj.put(context, key, arr, false);
            }
        }
        return obj;
    }

    private static String escape(String querystring) {
        try {
            return URLEncoder.encode(querystring, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return querystring;
        }
    }
    
    private static String unescape(String querystring) {
        try {
            return URLDecoder.decode(querystring, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return querystring;
        }
    }
}
