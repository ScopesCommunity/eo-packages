switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
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
    include "stb_vorbis.c"
        options "-DSTB_VORBIS_HEADER_ONLY"

..
    filter-scope header.extern "^stb_vorbis_"
    filter-scope header.typedef "^stb_vorbis_"
