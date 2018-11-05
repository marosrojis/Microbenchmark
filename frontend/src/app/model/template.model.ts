export class Template {
  public name: string;
  public libraries: string;
  public warmup: number;
  public measurement: number;
  public declare: string;
  public init: string;
  public testMethods: string[];

  constructor(name: string, warmup: number, measurement: number, declare: string, init: string, testMethods: string[]) {
    this.name = name;
    this.warmup = warmup;
    this.measurement = measurement;
    this.declare = declare;
    this.init = init;
    this.testMethods = testMethods;
  }
}
