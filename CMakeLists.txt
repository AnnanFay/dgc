PROJECT (dgc)

execute_process(COMMAND lein 
                OUTPUT_VARIABLE l)

message (STATUS "l=${l}")


install(DIRECTORY install/
        DESTINATION .
        PATTERN ".git" EXCLUDE)
