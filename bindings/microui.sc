switch operating-system
case 'linux
    shared-library "libmicroui.so"
case 'windows
    shared-library "microui.dll"
default
    error "Unsupported OS"

using import include

let header =
    include
        "microui.h"

do
    using header.extern  filter "^mu_(.+)$"
    using header.typedef filter "^mu_(.+)$"
    using header.struct  filter "^mu_(.+)$"
    using header.const   filter "^(MU_.+)$"
    using header.define   filter "^(MU_.+)$"

    inline button (ctx label)
        button_ex ctx label 0 MU_OPT_ALIGNCENTER

    inline textbox (ctx buf bufsz)
        button_ex ctx buf bufsz 0

    inline slider (ctx value lo hi)
        slider_ex ctx value lo hi 0 MU_SLIDER_FMT MU_OPT_ALIGNCENTER

    inline number (ctx value step)
        number_ex ctx value step MU_SLIDER_FMT MU_OPT_ALIGNCENTER

    inline header (ctx label)
        header_ex ctx label 0

    inline begin_treenode (ctx label)
        begin_treenode_ex ctx label 0

    inline begin_window (ctx title rect)
        begin_window_ex ctx title rect 0

    inline begin_panel (ctx name)
        begin_treenode_ex ctx name 0

    local-scope;
