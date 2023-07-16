switch operating-system
case 'linux
    shared-library "libmicroui.so"
case 'windows
    shared-library "microui.dll"
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
        "microui.h"

let mu-extern = (filter-scope header.extern "^mu_")
let mu-typedef = (filter-scope header.typedef "^mu_")
let mu-struct = (filter-scope header.struct "^mu_")

run-stage;

.. mu-extern mu-typedef mu-struct header.const
    do
        inline button (ctx label)
            mu-extern.button_ex ctx label 0 header.const.MU_OPT_ALIGNCENTER

        inline textbox (ctx buf bufsz)
            mu-extern.button_ex ctx buf bufsz 0

        inline slider (ctx value lo hi)
            mu-extern.slider_ex ctx value lo hi 0 header.const.MU_SLIDER_FMT header.const.MU_OPT_ALIGNCENTER

        inline number (ctx value step)
            mu-extern.number_ex ctx value step header.const.MU_SLIDER_FMT header.const.MU_OPT_ALIGNCENTER

        inline header (ctx label)
            mu-extern.header_ex ctx label 0

        inline begin_treenode (ctx label)
            mu-extern.begin_treenode_ex ctx label 0

        inline begin_window (ctx title rect)
            mu-extern.begin_window_ex ctx title rect 0

        inline begin_panel (ctx name)
            mu-extern.begin_treenode_ex ctx name 0

        locals;
