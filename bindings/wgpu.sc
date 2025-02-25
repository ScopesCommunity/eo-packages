switch operating-system
case 'linux
    shared-library "libwgpu_native.so"
case 'windows
    shared-library "wgpu_native.dll"
default
    error "Unsupported OS"

using import Array enum include slice

header := include "wgpu.h"

for k v in header.enum
    local old-symbols : (Array Symbol)
    T := (v as type)

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

let SIZE_MAX =
    header.define.SIZE_MAX

do
    using header.extern  filter "^wgpu(.+)$"
    using header.typedef filter "^WGPU(.+)$"
    using header.define  filter "^(WGPU_.+)$"

    let WGPU_ARRAY_LAYER_COUNT_UNDEFINED = 0xffffffff:u32
    let WGPU_COPY_STRIDE_UNDEFINED = 0xffffffff:u32
    let WGPU_LIMIT_U32_UNDEFINED = 0xffffffff:u32
    let WGPU_LIMIT_U64_UNDEFINED = 0xffffffffffffffff:u64
    let WGPU_MIP_LEVEL_COUNT_UNDEFINED = 0xffffffff:u32
    let WGPU_WHOLE_MAP_SIZE = SIZE_MAX
    let WGPU_WHOLE_SIZE = 0xffffffffffffffff:u64

    vvv bind InstanceBackend
    do
        using header.extern filter "^WGPUInstanceBackend_(.+)$"
        local-scope;

    vvv bind InstanceFlag
    do
        using header.extern filter "^WGPUInstanceFlag_(.+)$"
        local-scope;

    vvv bind BufferUsage
    do
        using header.extern filter "^WGPUBufferUsage_(.+)$"
        local-scope;

    vvv bind ColorWriteMask
    do
        using header.extern filter "^WGPUColorWriteMask_(.+)$"
        local-scope;

    vvv bind MapMode
    do
        using header.extern filter "^WGPUMapMode_(.+)$"
        local-scope;

    vvv bind ShaderStage
    do
        using header.extern filter "^WGPUShaderStage_(.+)$"
        local-scope;

    vvv bind TextureUsage
    do
        using header.extern filter "^WGPUTextureUsage_(.+)$"
        local-scope;

    local-scope;
