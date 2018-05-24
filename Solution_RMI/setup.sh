declare -a arr=("GeneralRepositoryOfInformation" "ControlCentreAndWatchingStand" "Paddock" "RaceTrack" "Stable" "BettingCentre" "Broker" "Horse" "Spectator")
declare -a pc_num=("01" "02" "03" "04" "05" "07" "08" "09" "10")

for i in {1..9}
do
    cd ${arr[i-1]}
    javac *.java
    jar cfe ../out/artifacts/${arr[i-1]}_jar/${arr[i-1]}.jar Main *.class
    rm *.class
    cd ../
    scp -r out/artifacts/${arr[i-1]}_jar/* sd0304@l040101-ws${pc_num[i-1]}.ua.pt:~/
    #ssh sd0304@l040101-ws${pc_num[i-1]}.ua.pt 'pkill -u sd0304' >/dev/null 2>&1
done

ssh sd0304@l040101-ws${pc_num[0]}.ua.pt java -jar ${arr[0]}.jar 22340 &
ssh sd0304@l040101-ws${pc_num[1]}.ua.pt java -jar ${arr[1]}.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws${pc_num[2]}.ua.pt java -jar ${arr[2]}.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws${pc_num[3]}.ua.pt java -jar ${arr[3]}.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws${pc_num[4]}.ua.pt java -jar ${arr[4]}.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws${pc_num[5]}.ua.pt java -jar ${arr[5]}.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws${pc_num[6]}.ua.pt java -jar ${arr[6]}.jar l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws${pc_num[7]}.ua.pt java -jar ${arr[7]}.jar l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws${pc_num[8]}.ua.pt java -jar ${arr[8]}.jar l040101-ws01.ua.pt 22340
