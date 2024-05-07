switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_voxel_render.h"

do
    using header.extern  filter "^stbvox_(.+)$"
    using header.typedef filter "^stbvox_(.+)$"
    local-scope;
