switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_hexwave.h"

do
    using header.extern  filter "^hexwave_(.+)$"
    using header.typedef filter "^([Hh]ex.+)$"
    local-scope;
