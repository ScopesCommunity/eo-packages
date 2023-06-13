switch operating-system
case 'linux
    shared-library "libglfw.so"
case 'windows
    shared-library "glfw3.dll"
default
    error "Unsupported OS"
                                                           
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
        "GLFW/glfw3.h"                                     
                                                           
let glfw-extern = (filter-scope header.extern "^glfw")     
let glfw-typedef = (filter-scope header.typedef "^GLFW")   
let glfw-define = (filter-scope header.define "^(?=GLFW_)")
                                                           
.. glfw-extern glfw-typedef glfw-define                    
