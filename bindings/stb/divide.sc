shared-library "libstb.so"

inline filter-scope (scope pattern)
    pattern as:= string
    fold (scope = (Scope)) for k v in scope
        let name = (k as Symbol as string)
        let match? start end = ('match? pattern name)
        if match?
            'bind scope (Symbol (rslice name end)) v
        else
            scope

let header =
    include "stb_divide.h"

filter-scope header.extern "^stb_"
