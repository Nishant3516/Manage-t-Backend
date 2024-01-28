import * as dayjs from 'dayjs';
import { VendorType } from 'app/entities/enumerations/vendor-type.model';

export interface IVendors {
  id?: number;
  vendorPhotoContentType?: string | null;
  vendorPhoto?: string | null;
  vendorPhotoLink?: string | null;
  vendorId?: string | null;
  vendorName?: string;
  phoneNumber?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  nickName?: string | null;
  email?: string | null;
  createDate?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  vendorType?: VendorType | null;
}

export class Vendors implements IVendors {
  constructor(
    public id?: number,
    public vendorPhotoContentType?: string | null,
    public vendorPhoto?: string | null,
    public vendorPhotoLink?: string | null,
    public vendorId?: string | null,
    public vendorName?: string,
    public phoneNumber?: string | null,
    public dateOfBirth?: dayjs.Dayjs | null,
    public addressLine1?: string | null,
    public addressLine2?: string | null,
    public nickName?: string | null,
    public email?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public vendorType?: VendorType | null
  ) {}
}

export function getVendorsIdentifier(vendors: IVendors): number | undefined {
  return vendors.id;
}
