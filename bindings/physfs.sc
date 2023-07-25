switch operating-system
case 'linux
    shared-library "libphysfs.so"
case 'windows
    shared-library "physfs.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include
        "physfs.h"

let physfs-extern = (filter-scope header.extern "^PHYSFS_")
let physfs-typedef = (filter-scope header.typedef "^PHYSFS_")
let physfs-define = (filter-scope header.define "^PHYSFS_")

.. physfs-extern physfs-typedef physfs-define
