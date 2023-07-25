switch operating-system
case 'linux
    shared-library "libfontdue_native.so"
case 'windows
    shared-library "fontdue_native.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include
        "fontdue.h"

let fontdue-extern = (filter-scope header.extern "^ftd_")
let fontdue-typedef = (filter-scope header.typedef "^FTD_")

.. fontdue-extern fontdue-typedef
