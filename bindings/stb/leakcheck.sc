switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "libstb.dll"
default
    error "Unsupported OS"

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
    include "stb_leakcheck.h"

filter-scope header.extern "^stb_leakcheck_"
