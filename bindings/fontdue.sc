switch operating-system
case 'linux
    shared-library "libfontdue_native.so"
case 'windows
    shared-library "fontdue_native.dll"
default
    error "Unsupported OS"

using import slice

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
    include
        "fontdue.h"

let fontdue-extern = (filter-scope header.extern "^ftd_")
let fontdue-typedef = (filter-scope header.typedef "^FTD_")

.. fontdue-extern fontdue-typedef
