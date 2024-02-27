header := include "cgltf_write.h"

using import slice

do
    using header.extern filter "^cgltf_(.+)$"
    using header.typedef filter "^cgltf_(.+)$"
    using header.enum filter "^cgltf_(.+)$"

    local-scope;
