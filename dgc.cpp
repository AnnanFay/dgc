// This is the first C/C++ program I've written. Apollogies for any stupid code.

#include "dgc.h"

DFhackCExport command_result dgc (Core * c, vector <string> & parameters);

DFhackCExport const char * plugin_name ( void ) {
    return "dgc";
}

DFhackCExport command_result plugin_init ( Core * c, std::vector <PluginCommand> &commands) {
    commands.clear();
    commands.push_back(PluginCommand("dgc",
               "DGC DESCRIPTION",
                dgc));
    return CR_OK;
}

DFhackCExport command_result plugin_shutdown ( Core * c ) {
    return CR_OK;
}

JNIEnv* create_vm(JavaVM ** jvm) {

    JNIEnv *env;
    JavaVMInitArgs vm_args;
    JavaVMOption options;
    options.optionString = "-Djava.class.path=."; //Path to the java source code
    vm_args.version = JNI_VERSION_1_6; //JDK version. This indicates version 1.6
    vm_args.nOptions = 1;
    vm_args.options = &options;
    vm_args.ignoreUnrecognized = 0;

    int ret = JNI_CreateJavaVM(jvm, (void**) &env, &vm_args);
    if (ret < 0)
        printf("\nUnable to Launch JVM\n");
    return env;
}

DFhackCExport command_result dgc (Core * c, vector <string> & parameters)
{
    // Create our JVM
    JNIEnv *env;
    JavaVM * jvm;
    env = create_vm(&jvm);
    if (env == NULL){
        return 1;
    }

    //Obtaining Class
    jclass Hello = env->FindClass("Hello");
    jmethodID HelloMain;

    //Obtaining Method IDs
    if (Hello != NULL) {
        HelloMain = env->GetStaticMethodID(Hello, "main", "([Ljava/lang/String;)V");
    } else {
        printf("\nUnable to find the requested class\n");
    }
    // Now we will call the functions using the their method IDs
    if (HelloMain != NULL){
        env->CallStaticVoidMethod(Hello, HelloMain, NULL); //Calling the main method.
    } else {
        printf("\nUnable to find the requested method\n");
    }

    //Release resources.
    int n = jvm->DestroyJavaVM();

    return CR_OK;
}
