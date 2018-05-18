javac interfaces/*.java registry/*.java serverSide/*.java clientSide/*.java
cp interfaces/Register.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/
cp interfaces/*.class dir_serverSide/interfaces/
cp serverSide/*.class dir_serverSide/serverSide/
cp interfaces/Compute.class interfaces/Task.class dir_clientSide/interfaces/
cp clientSide/*.class dir_clientSide/clientSide/
mkdir -p /home/ruib/Public/classes
mkdir -p /home/ruib/Public/classes/interfaces
mkdir -p /home/ruib/Public/classes/clientSide
cp interfaces/*.class /home/ruib/Public/classes/interfaces
cp clientSide/Pi.class /home/ruib/Public/classes/clientSide
cp set_rmiregistry.sh /home/ruib
cp set_rmiregistry_alt.sh /home/ruib
