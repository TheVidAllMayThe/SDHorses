#!/usr/bin/env bash
declare -a arr=("GeneralRepositoryOfInformation" "ControlCentreAndWatchingStand" "Paddock" "RaceTrack" "Stable" "BettingCentre" "Broker" "Horse" "Spectator")
for i in {1..9}
do
    ssh sd0304@l040101-ws0$i.ua.pt 'bash -l -c "fuser -k 22340/tcp"' 
done
