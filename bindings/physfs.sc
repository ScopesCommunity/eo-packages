switch operating-system
case 'linux
    shared-library "libphysfs.so"
case 'windows
    shared-library "physfs.dll"
default
    error "Unsupported OS"

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

let header =
    include
        "physfs.h"

let physfs-extern = (filter-scope header.extern "^PHYSFS_")
let physfs-typedef = (filter-scope header.typedef "^PHYSFS_")
let physfs-define = (filter-scope header.define "^PHYSFS_")

.. physfs-extern physfs-typedef physfs-define
