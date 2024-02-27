header := include "cgltf_write.h"

using import Array slice

for k v in header.typedef
    T := v as type
    if (T < CEnum)
        local old-symbols : (Array Symbol)
        tname := k as Symbol as string
        for k v in ('symbols T)
            field-name := k as Symbol as string
            if ('match? str"^cgltf_" field-name)
                new-name := rslice field-name ((countof tname) + 1) # _
                'set-symbol T (Symbol new-name) v
                'append old-symbols (k as Symbol)

        for s in old-symbols
            sc_type_del_symbol T s

do
    using header.extern filter "^cgltf_(.+)$"
    using header.typedef filter "^cgltf_(.+)$"
    using header.enum filter "^cgltf_(.+)$"

    local-scope;
