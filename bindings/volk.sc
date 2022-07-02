switch operating-system
case 'linux
    shared-library "libvolk.so"
case 'windows
    shared-library "libvolk.dll"
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
        "volk.h"

let volk-extern = (filter-scope header.extern "(^vk|(?=volk))")
let volk-typedef = (filter-scope header.typedef "^Vk")
let volk-define = (filter-scope header.define "^(?=VK_)")

.. volk-extern volk-typedef volk-define
