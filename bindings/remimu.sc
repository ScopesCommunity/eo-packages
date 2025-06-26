switch operating-system
case 'linux
    shared-library "libremimu.so"
case 'windows
    shared-library "remimu.dll"
default
    error "Unsupported OS"

using import include
header := 
    include
        """"#include <stdint.h>
            #include <stddef.h>

            int REMIMU_FLAG_DOT_NO_NEWLINES = 1;

            uint8_t REMIMU_KIND_NORMAL      = 0;
            uint8_t REMIMU_KIND_OPEN        = 1;
            uint8_t REMIMU_KIND_NCOPEN      = 2;
            uint8_t REMIMU_KIND_CLOSE       = 3;
            uint8_t REMIMU_KIND_OR          = 4;
            uint8_t REMIMU_KIND_CARET       = 5;
            uint8_t REMIMU_KIND_DOLLAR      = 6;
            uint8_t REMIMU_KIND_BOUND       = 7;
            uint8_t REMIMU_KIND_NBOUND      = 8;
            uint8_t REMIMU_KIND_END         = 9;

            uint8_t REMIMU_MODE_POSSESSIVE  = 1;
            uint8_t REMIMU_MODE_LAZY        = 2;
            uint8_t REMIMU_MODE_INVERTED    = 128; // temporary; gets cleared later

            typedef struct _RegexToken {
                uint8_t kind;
                uint8_t mode;
                uint16_t count_lo;
                uint16_t count_hi; // 0 means no limit
                uint16_t mask[16]; // for groups: mask 0 stores group-with-quantifier number (quantifiers are +, *, ?, {n}, {n,}, or {n,m})
                int16_t pair_offset; // from ( or ), offset in token list to matching paren. TODO: move into mask maybe
            } RegexToken;

            int regex_parse(const char * pattern, RegexToken * tokens, int16_t * token_count, int32_t flags);
            int64_t regex_match(const RegexToken * tokens, const char * text, size_t start_i, uint16_t cap_slots, int64_t * cap_pos, int64_t * cap_span);
            void print_regex_tokens(RegexToken * tokens);

do
    using header.typedef filter "^Regex.+$"
    using header.const filter "^REMIMU.+$"
    using header.extern filter "^REMIMU.+$"
    regex_match := header.extern.regex_match
    regex_parse := header.extern.regex_parse
    print_regex_tokens := header.extern.print_regex_tokens
    local-scope;
