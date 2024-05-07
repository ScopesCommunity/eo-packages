switch operating-system
case 'linux
    shared-library "libcjson.so"
case 'windows
    shared-library "cjson.dll"
default
    error "Unsupported OS"

using import include

header := include "cJSON.h"

do
    cJSON_Invalid := 0
    cJSON_False   := (1 << 0)
    cJSON_True    := (1 << 1)
    cJSON_NULL    := (1 << 2)
    cJSON_Number  := (1 << 3)
    cJSON_String  := (1 << 4)
    cJSON_Array   := (1 << 5)
    cJSON_Object  := (1 << 6)
    cJSON_Raw     := (1 << 7)

    using header.define filter "^CJSON_.+$"
    using header.extern filter "^cJSON_(.+)$"
    using header.typedef filter "^cJSON_(.+)$"
    local-scope;
