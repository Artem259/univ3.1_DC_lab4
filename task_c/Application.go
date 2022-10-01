package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Graph struct {
	tb [][]int
	mx sync.RWMutex
}

func twoRandomIndices(maxIndex int) [2]int {
	var res [2]int
	res[0] = rand.Intn(maxIndex + 1)
	res[1] = rand.Intn(maxIndex)
	if res[0] == res[1] {
		res[1] = maxIndex
	}
	return res
}

func printMatrix(matrix [][]int) {
	for i := 0; i < len(matrix); i++ {
		for j := 0; j < len(matrix[i]); j++ {
			fmt.Printf("%v ", matrix[i][j])
		}
		fmt.Print("\n")
	}
}

func addDeletePaths(graph *Graph) {
	for true {
		graph.mx.Lock()
		size := len(graph.tb)
		if size > 1 {
			var path = twoRandomIndices(size - 1)
			if graph.tb[path[0]][path[1]] != 0 {
				graph.tb[path[0]][path[1]] = 0
				graph.tb[path[1]][path[0]] = graph.tb[path[0]][path[1]]
				fmt.Printf(" - Path: %v --- %v\n", path[0], path[1])
			} else {
				graph.tb[path[0]][path[1]] = rand.Intn(10)
				graph.tb[path[1]][path[0]] = graph.tb[path[0]][path[1]]
				fmt.Printf(" + Path: %v <-> %v\n", path[0], path[1])
			}
			printMatrix(graph.tb)
			fmt.Print("---------------\n")
		}
		graph.mx.Unlock()
		time.Sleep(time.Duration(2000+rand.Intn(1000)) * time.Millisecond)
	}
}

func addDeleteCities(graph *Graph) {
	for true {
		graph.mx.Lock()
		oldSize := len(graph.tb)
		if (rand.Intn(2) == 0 && oldSize > 3) || oldSize > 9 {
			toDelete := rand.Intn(oldSize)
			graph.tb = append(graph.tb[:toDelete], graph.tb[toDelete+1:]...)
			for i := 0; i < oldSize-1; i++ {
				graph.tb[i] = append(graph.tb[i][:toDelete], graph.tb[i][toDelete+1:]...)
			}
			fmt.Printf(" - City: %v now\n", oldSize-1)
		} else {
			graph.tb = append(graph.tb, make([]int, oldSize))
			for i := 0; i < oldSize+1; i++ {
				graph.tb[i] = append(graph.tb[i], 0)
			}
			fmt.Printf(" + City: %v now\n", oldSize+1)
		}
		printMatrix(graph.tb)
		fmt.Print("---------------\n")
		graph.mx.Unlock()
		time.Sleep(time.Duration(2000+rand.Intn(1000)) * time.Millisecond)
	}
}

func main() {
	rand.Seed(time.Now().Unix())
	var graph Graph

	fmt.Print("---------------\n")
	go addDeleteCities(&graph)
	go addDeletePaths(&graph)

	time.Sleep(180 * time.Second)
}
