switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_leakcheck.h"

do
    using header.extern filter "^stb_leakcheck_(.+)$"
    local-scope;
