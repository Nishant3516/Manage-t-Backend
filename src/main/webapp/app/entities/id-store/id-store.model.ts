import * as dayjs from 'dayjs';
import { ISchool } from 'app/entities/school/school.model';
import { IdType } from 'app/entities/enumerations/id-type.model';

export interface IIdStore {
  id?: number;
  entrytype?: IdType;
  lastGeneratedId?: number;
  startId?: number | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  school?: ISchool | null;
}

export class IdStore implements IIdStore {
  constructor(
    public id?: number,
    public entrytype?: IdType,
    public lastGeneratedId?: number,
    public startId?: number | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public school?: ISchool | null
  ) {}
}

export function getIdStoreIdentifier(idStore: IIdStore): number | undefined {
  return idStore.id;
}
