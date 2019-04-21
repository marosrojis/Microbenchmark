export class ResultBenchmark {
  public name: string;
  public warmupIterations: number;
  public measurementIterations: number;
  public unit: string;
  public score: number;
  public error: number;
  public measureValues: number[];

  constructor(
    name: string,
    warmupIterations: number,
    measurementIterations: number,
    unit: string,
    score: number,
    error: number,
    measureValues: number[]
  ) {
    name = this.name;
    warmupIterations = this.warmupIterations;
    measurementIterations = this.measurementIterations;
    unit = this.unit;
    score = this.score;
    error = this.error;
    measureValues = this.measureValues;
  }
}
