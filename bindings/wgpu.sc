switch operating-system
case 'linux
    shared-library "libwgpu_native.so"
case 'windows
    shared-library "wgpu_native.dll"
default
    error "Unsupported OS"

using import Array
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

header := include "wgpu.h"

let wgpu-extern = (filter-scope header.extern "^wgpu")
let wgpu-typedef = (filter-scope header.typedef "^WGPU")

let SIZE_MAX =
    static-if (operating-system == 'windows)
        header.define.SIZE_MAX
    else
        header.define.__SIZE_MAX__

wgpu-define :=
    ..
        do
            let WGPU_ARRAY_LAYER_COUNT_UNDEFINED = 0xffffffff:u32
            let WGPU_COPY_STRIDE_UNDEFINED = 0xffffffff:u32
            let WGPU_LIMIT_U32_UNDEFINED = 0xffffffff:u32
            let WGPU_LIMIT_U64_UNDEFINED = 0xffffffffffffffff:u64
            let WGPU_MIP_LEVEL_COUNT_UNDEFINED = 0xffffffff:u32
            let WGPU_WHOLE_MAP_SIZE = SIZE_MAX
            let WGPU_WHOLE_SIZE = 0xffffffffffffffff:u64
            locals;
        filter-scope header.define "^(?=WGPU_)"

inline enum-constructor (T)
    bitcast 0 T

for k v in header.enum
    local old-symbols : (Array Symbol)
    T := (v as type)
    'set-symbol T '__typecall enum-constructor

    for k v in ('symbols T)
        original-symbol  := k as Symbol
        original-name    := original-symbol as string
        match? start end := 'match? str"^WGPU.+_" original-name

        if match?
            field := (Symbol (rslice original-name end))
            'set-symbol T field v
            'append old-symbols original-symbol

    for sym in old-symbols
        sc_type_del_symbol T sym

run-stage;

.. wgpu-extern wgpu-typedef wgpu-define
