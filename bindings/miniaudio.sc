switch operating-system
case 'linux
    shared-library "libminiaudio.so"
case 'windows
    shared-library "miniaudio.dll"
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
        "miniaudio.h"

let miniaudio-extern = (filter-scope header.extern "^ma_")
let miniaudio-typedef = (filter-scope header.typedef "^ma_")
let miniaudio-define = (filter-scope header.define "^(?=MA_)")
let miniaudio-const = (filter-scope header.const "^(?=MA_)")

.. miniaudio-extern miniaudio-typedef miniaudio-define miniaudio-const
