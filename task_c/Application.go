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

func BFS(graph *Graph, start, goal int) int {
	frontier := make([]int, 0)
	frontier = append(frontier, start)
	cameFrom := map[int]int{
		start: -1,
	}

	found := false
	for len(frontier) != 0 {
		current := frontier[0]
		frontier = frontier[1:]

		if current == goal {
			found = true
			break
		}
		for i, next := range graph.tb[current] {
			if next == 0 {
				continue
			}
			if _, ok := cameFrom[i]; !ok {
				frontier = append(frontier, i)
				cameFrom[i] = current
			}
		}
	}
	if !found {
		return 0
	}

	current := goal
	path := make([]int, 0)
	for current != start {
		path = append(path, current)
		current = cameFrom[current]
	}

	path = append(path, start)
	result := 0
	for i := 1; i < len(path); i++ {
		result += graph.tb[path[i-1]][path[i]]
	}
	return result
}

func deleteCity(graph *Graph, index int) {
	graph.tb = append(graph.tb[:index], graph.tb[index+1:]...)
	for i := 0; i < len(graph.tb); i++ {
		graph.tb[i] = append(graph.tb[i][:index], graph.tb[i][index+1:]...)
	}
}

func updatePaths(graph *Graph) {
	for true {
		graph.mx.Lock()
		size := len(graph.tb)
		if size > 1 {
			var path = twoRandomIndices(size - 1)
			if graph.tb[path[0]][path[1]] != 0 {
				graph.tb[path[0]][path[1]] = 1 + rand.Intn(8)
				if graph.tb[path[0]][path[1]] == graph.tb[path[1]][path[0]] {
					graph.tb[path[0]][path[1]] = 9
				}
				graph.tb[path[1]][path[0]] = graph.tb[path[0]][path[1]]
				fmt.Printf(" @ Path %v-%v updated: %v now\n", path[0], path[1], graph.tb[path[0]][path[1]])
				printMatrix(graph.tb)
				fmt.Print("---------------\n")
			}
		}
		graph.mx.Unlock()
		time.Sleep(time.Duration(4000+rand.Intn(2000)) * time.Millisecond)
	}
}

func addDeletePaths(graph *Graph) {
	for true {
		graph.mx.Lock()
		size := len(graph.tb)
		if size > 1 {
			for attempt := 0; attempt < 3; attempt++ {
				var path = twoRandomIndices(size - 1)
				if graph.tb[path[0]][path[1]] != 0 {
					if attempt == 2 {
						graph.tb[path[0]][path[1]] = 0
						graph.tb[path[1]][path[0]] = graph.tb[path[0]][path[1]]
						fmt.Printf(" - Path: %v --- %v\n", path[0], path[1])
					}
				} else {
					graph.tb[path[0]][path[1]] = 1 + rand.Intn(9)
					graph.tb[path[1]][path[0]] = graph.tb[path[0]][path[1]]
					fmt.Printf(" + Path: %v <-> %v\n", path[0], path[1])
					break
				}
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
			deleteCity(graph, toDelete)
			fmt.Printf(" - City %v: %v now\n", toDelete, oldSize-1)
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
		time.Sleep(time.Duration(5000+rand.Intn(2500)) * time.Millisecond)
	}
}

func pathFinding(graph *Graph) {
	for true {
		graph.mx.RLock()
		size := len(graph.tb)
		if size > 1 {
			var path = twoRandomIndices(size - 1)
			distance := BFS(graph, path[0], path[1])
			if distance != 0 {
				fmt.Printf(" D Distance %v-%v is %v\n", path[0], path[1], distance)
			} else {
				fmt.Printf(" D Distance %v-%v is [no path]\n", path[0], path[1])
			}
			fmt.Print("---------------\n")
		}
		graph.mx.RUnlock()
		time.Sleep(time.Duration(10000+rand.Intn(5000)) * time.Millisecond)
	}
}

func main() {
	rand.Seed(time.Now().Unix())
	var graph Graph

	fmt.Print("---------------\n")
	go addDeleteCities(&graph)
	go addDeletePaths(&graph)
	go updatePaths(&graph)
	go pathFinding(&graph)

	time.Sleep(180 * time.Second)
}
