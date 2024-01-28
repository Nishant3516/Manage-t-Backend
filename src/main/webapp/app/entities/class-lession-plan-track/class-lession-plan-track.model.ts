import * as dayjs from 'dayjs';
import { IClassLessionPlan } from 'app/entities/class-lession-plan/class-lession-plan.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';

export interface IClassLessionPlanTrack {
  id?: number;
  workStatus?: TaskStatus;
  remarks?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classLessionPlan?: IClassLessionPlan | null;
}

export class ClassLessionPlanTrack implements IClassLessionPlanTrack {
  constructor(
    public id?: number,
    public workStatus?: TaskStatus,
    public remarks?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classLessionPlan?: IClassLessionPlan | null
  ) {}
}

export function getClassLessionPlanTrackIdentifier(classLessionPlanTrack: IClassLessionPlanTrack): number | undefined {
  return classLessionPlanTrack.id;
}
