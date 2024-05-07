switch operating-system
case 'linux
    shared-library "libphysfs.so"
case 'windows
    shared-library "physfs.dll"
default
    error "Unsupported OS"

using import Array include slice

header := include "physfs.h"

for k v in header.typedef
    if (('typeof v) != type)
        continue;

    local old-symbols : (Array Symbol)
    T := (v as type)
    if (T < CEnum)
        for k v in ('symbols T)
            original-symbol  := k as Symbol
            original-name    := original-symbol as string
            match? start end := 'match? str"^PHYSFS_" original-name

            if match?
                field := (Symbol (rslice original-name end))
                'set-symbol T field v
                'append old-symbols original-symbol

        for sym in old-symbols
            sc_type_del_symbol T sym

do
    using header.extern  filter "^PHYSFS_(.+)$"
    using header.typedef filter "^PHYSFS_(.+)$"
    using header.define  filter "^PHYSFS_(.+)$"

    local-scope;
