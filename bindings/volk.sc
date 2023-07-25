switch operating-system
case 'linux
    shared-library "libvolk.so"
case 'windows
    shared-library "libvolk.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include
        "volk.h"

let volk-extern = (filter-scope header.extern "(^vk|(?=volk))")
let volk-typedef = (filter-scope header.typedef "^Vk")
let volk-define = (filter-scope header.define "^(?=VK_)")

.. volk-extern volk-typedef volk-define
