switch operating-system
case 'linux
    shared-library "libwgpu_native.so"
case 'windows
    shared-library "wgpu_native.dll"
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
    include
        "wgpu.h"

let wgpu-extern = (filter-scope header.extern "^wgpu")
let wgpu-typedef = (filter-scope header.typedef "^WGPU")

let SIZE_MAX =
    static-if (operating-system == 'windows)
        header.define.SIZE_MAX
    else
        header.define.__SIZE_MAX__

let wgpu-define =
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

vvv bind wgpu-enum
fold (scope = (Scope)) for k v in header.enum
    let T = (v as type)
    for k v in ('symbols T)
        let sname = (k as Symbol as string)
        let match? start end = ('match? str"^.+_" sname)
        if match?
            'set-symbol T (Symbol (rslice sname end)) v
            'set-symbol T '__typecall enum-constructor

    # we already know all enums here should match WGPU prefix.
    let name = (rslice (k as Symbol as string) (countof "WGPU"))
    'bind scope (Symbol name) v

.. wgpu-enum wgpu-extern wgpu-typedef wgpu-define
