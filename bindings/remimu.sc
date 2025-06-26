using import include
header := 
    include  "remimu.h"
        options "-DREMIMU_FUNC_VISIBILITY=extern"

do
    using header.typedef filter "^Regex.+$"
    using header.const filter "^REMIMU.+$"
    using header.extern filter "^REMIMU.+$"
    regex_match := header.extern.regex_match
    regex_parse := header.extern.regex_parse
    print_regex_tokens := header.extern.print_regex_tokens
    local-scope;
