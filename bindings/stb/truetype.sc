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

# because stb_truetype depends on some types from rect_pack, the way we built it.
stbrp := ((import .rect-pack) . !header)
run-stage;

let header =
    include "stb_truetype.h"
        using stbrp

..
    filter-scope header.extern "^stbtt_"
    filter-scope header.typedef "^stbtt_"
