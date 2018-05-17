declare -a arr=("GeneralRepositoryOfInformation" "ControlCentreAndWatchingStand" "Paddock" "RaceTrack" "Stable" "BettingCentre" "Broker" "Horse" "Spectator")

for i in {1..9}
do
    cd ${arr[i-1]}
    javac *.java
    jar cfe ../out/artifacts/${arr[i-1]}_jar/${arr[i-1]}.jar Main *.class
    rm *.class
    cd ../
    scp -r out/artifacts/${arr[i-1]}_jar/* sd0304@l040101-ws0$i.ua.pt:~/
    #ssh sd0304@l040101-ws0$i.ua.pt 'bash -l -c "fuser -k 22340/tcp"' >/dev/null 2>&1
done

ssh sd0304@l040101-ws01.ua.pt java -jar *.jar 22340 &
ssh sd0304@l040101-ws02.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws03.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws04.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws05.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws06.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws07.ua.pt java -jar *.jar l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws08.ua.pt java -jar *.jar l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws09.ua.pt java -jar *.jar l040101-ws01.ua.pt 22340
