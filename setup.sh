scp -r out/artifacts/General_Repository_Of_Information_jar/* sd0304@l040101-ws01.ua.pt:~/
scp -r out/artifacts/Betting_Centre_jar/* sd0304@l040101-ws02.ua.pt:~/
scp -r out/artifacts/Control_Centre_And_Watching_Stand_jar/* sd0304@l040101-ws03.ua.pt:~/
scp -r out/artifacts/Paddock_jar/* sd0304@l040101-ws04.ua.pt:~/
scp -r out/artifacts/Race_Track_jar/* sd0304@l040101-ws05.ua.pt:~/
scp -r out/artifacts/Stable_jar/* sd0304@l040101-ws06.ua.pt:~/
scp -r out/artifacts/Broker_jar/* sd0304@l040101-ws07.ua.pt:~/
scp -r out/artifacts/Spectator_jar/* sd0304@l040101-ws08.ua.pt:~/
scp -r out/artifacts/Horse_jar/* sd0304@l040101-ws09.ua.pt:~/

ssh sd0304@l040101-ws01.ua.pt java -jar *.jar 22340 &
ssh sd0304@l040101-ws02.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws03.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws04.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws05.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws06.ua.pt java -jar *.jar 22340 l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws07.ua.pt java -jar *.jar l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws08.ua.pt java -jar *.jar l040101-ws01.ua.pt 22340 &
ssh sd0304@l040101-ws09.ua.pt java -jar *.jar l040101-ws01.ua.pt 22340
