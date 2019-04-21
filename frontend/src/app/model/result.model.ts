import { ResultBenchmark } from './result-benchmark.model';

export class Result {
  public results: ResultBenchmark[];
  public bestScoreIndex: number;
  public numberOfConnections: number;
  public time: string;

  constructor(results: ResultBenchmark[], bestScoreIndex: number, numberOfConnections: number, time: string) {
    results = this.results;
    bestScoreIndex = this.bestScoreIndex;
    numberOfConnections = this.numberOfConnections;
    time = this.time;
  }
}
