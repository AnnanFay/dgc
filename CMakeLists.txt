PROJECT (dgc)

# Include JNI
find_package(JNI)

if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif()

include_directories (
    .
    ${CMAKE_SOURCE_DIR}
    ${JNI_INCLUDE_DIRS}
)

# Some globbing
FILE(GLOB PROJECT_SRCS *.cpp)
FILE(GLOB PROJECT_HDRS *.h)

SET_SOURCE_FILES_PROPERTIES( ${PROJECT_HDRS} PROPERTIES HEADER_FILE_ONLY TRUE)

SET(PROJECT_LIBS
    ${PROJECT_LIBS}
    ${JNI_LIBRARIES}
)


# mash them together (headers are marked as headers and nothing will try to compile them)
LIST(APPEND PROJECT_SRCS ${PROJECT_HDRS})

#linux
IF(UNIX)
    add_definitions(-DLINUX_BUILD)
    SET(PROJECT_LIBS
        # add any extra linux libs here
        ${PROJECT_LIBS}
    )
# windows
ELSE(UNIX)
    SET(PROJECT_LIBS
        # add any extra linux libs here
        ${PROJECT_LIBS}
        $(NOINHERIT)
    )
ENDIF(UNIX)

# this makes sure all the stuff is put in proper places and linked to dfhack
DFHACK_PLUGIN(dgc ${PROJECT_SRCS} LINK_LIBRARIES ${PROJECT_LIBS})

install(DIRECTORY gui/
        DESTINATION "dgc"
        PATTERN ".git" EXCLUDE)

