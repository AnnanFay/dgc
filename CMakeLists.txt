PROJECT (dgc)

message (STATUS "Building DGC... ")

execute_process(COMMAND lein uberjar
                WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR})

install(FILES dgc-1.0.0-SNAPSHOT-standalone.jar 
        DESTINATION .)
