export class Template {
  public libraries: string;
  public warmup: number;
  public measurement: number;
  public declare: string;
  public init: string;
  public testMethods: string[];

  constructor(warmup: number, measurement: number, declare: string, init: string, testMethods: string[]) {
    this.warmup = warmup;
    this.measurement = measurement;
    this.declare = declare;
    this.init = init;
    this.testMethods = testMethods;
  }
}
