PROJECT (dgc)

# If we're on unix we can call a bash script to check if the sources have been updated since last build
# Otherwise we rebuild everytime
IF(UNIX)
	add_custom_target(dgc ALL
	                   COMMAND ./build.sh
	                   WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR})
ELSE()
	add_custom_target(dgc ALL
	                   COMMAND lein uberjar
	                   WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR})
ENDIF()

#copy jar and data to df
install(DIRECTORY .
        DESTINATION dgc
		FILES_MATCHING PATTERN "dgc-*-standalone.jar")

install(DIRECTORY data
        DESTINATION dgc)
